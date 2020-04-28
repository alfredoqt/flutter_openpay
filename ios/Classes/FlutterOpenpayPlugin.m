#import "FlutterOpenpayPlugin.h"
#import "Openpay.h"

@implementation FlutterOpenpayPlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"flutter_openpay"
            binaryMessenger:[registrar messenger]];
  FlutterOpenpayPlugin* instance = [[FlutterOpenpayPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  }else if ([@"getDeviceSessionId" isEqualToString:call.method]) {
    NSString* MERCHANT_ID = call.arguments[@"merchantId"];
    NSString* API_KEY = call.arguments[@"publicApiKey"];
    NSNumber* productionMode = call.arguments[@"productionMode"];
    BOOL isProductionMode = ([productionMode boolValue] == YES);

    result([[[Openpay alloc] initWithMerchantId:MERCHANT_ID apyKey:API_KEY isProductionMode:isProductionMode] createDeviceSessionId]);
  }else if ([@"tokenizeCard" isEqualToString:call.method]) {
    NSString* MERCHANT_ID = call.arguments[@"merchantId"];
    NSString* API_KEY = call.arguments[@"publicApiKey"];
    NSNumber* productionMode = call.arguments[@"productionMode"];
    BOOL isProductionMode = ([productionMode boolValue] == YES);

    OPCard *card = [[OPCard alloc]init];
    card.holderName = call.arguments[@"cardholderName"];
    card.number = call.arguments[@"cardNumber"];
    card.expirationMonth = call.arguments[@"expiryMonth"];
    card.expirationYear = call.arguments[@"expiryYear"];
    card.cvv2 = call.arguments[@"cvv"];

    [[[Openpay alloc] initWithMerchantId:MERCHANT_ID apyKey:API_KEY isProductionMode:isProductionMode] createTokenWithCard:card
      success:^(OPToken *token) {

        result(token.id);
      } failure:^(NSError *error) {

        result([error localizedDescription]);
    }];
  }else {
    result(FlutterMethodNotImplemented);
  }
}

@end
