import 'dart:async';

import 'dart:convert';
import 'package:http/http.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

// The entry point of the FlutterOpenpay SDK
class FlutterOpenpay {
  static const MethodChannel _channel = const MethodChannel('flutter_openpay');

  /// Creates a token for a card which is ready
  /// to process using Openpay API.
  ///
  /// Errors:
  ///   • `ERROR_INTERNAL_SERVER_ERROR` - An error happened in the internal Openpay server.
  ///   • `ERROR_BAD_REQUEST` - The request is not JSON valid format, the fields don’t have the correct format, or the request doesn’t have the required fields.
  ///   • `ERROR_UNAUTHORIZED` - The request is not authenticated or is incorrect.
  ///   • `ERROR_UNPROCESSABLE_ENTITY` - The operation couldn’t be processed because one or more parameters are incorrect.
  ///   • `ERROR_SERVICE_UNAVAILABLE` - A required service is not available.
  ///   • `ERROR_NOT_FOUND` - A required resource doesn’t exist.
  ///   • `ERROR_REQUEST_ENTITY_TOO_LARGE` - The request body is too large.
  ///   • `ERROR_PUBLIC_KEY_NOT_ALLOWED` - The public key is being used to make a request that requires the private key.
  ///   • `ERROR_INVALID_CARD_NUMBER` - The identifier digit of this card number is invalid according to Luhn algorithm.
  ///   • `ERROR_INVALID_EXP_DATE` - The card expiration date is prior to the current date.
  ///   • `ERROR_CVV2_MISSING` - The card security code (CVV2) wasn’t provided.
  ///   • `ERROR_CARD_NUMBER_ONLY_SANDBOX` - The card number is just for testing, it can only be used in Sandbox.
  ///   • `ERROR_INVALID_CVV2` - The card security code (CVV2) is invalid.
  ///   • `ERROR_CARD_PRODUCT_TYPE_NOT_SUPPORTED` - 	Card product type not supported.
  ///   • `ERROR_CARD_DECLINED` - Card declined.
  ///   • `ERROR_CARD_EXPIRED` - Card is expired.
  ///   • `ERROR_CARD_INSUFFICIENT_FUNDS` - Card has not enough funds.
  ///   • `ERROR_CARD_STOLEN` - Card has been flagged as stolen.
  ///   • `ERROR_CARD_FRAUDULENT` - 	Card has been rejected by the antifraud system.
  ///   • `ERROR_CARD_NOT_SUPPORTED_IN_ONLINE_TRANSACTIONS` - The card doesn’t support online transactions.
  ///   • `ERROR_CARD_REPORTED_AS_LOST` - Card has been flagged as lost.
  ///   • `ERROR_CARD_RESTRICTED_BY_BANK` - The card has been restricted by the bank.
  ///   • `ERROR_CARD_RETAINED_BY_BANK` - The bank has requested to hold this card. Please contact the bank.
  ///   • `ERROR_SERVICE_UNAVAILABLE` - The service is unavailable. Might be due to no internet connection.
  static Future<String> tokenizeCard({
    @required String merchantId,
    @required String publicApiKey,
    @required bool productionMode,
    @required String cardholderName,
    @required String cardNumber,
    @required String cvv,
    @required String expiryMonth,
    @required String expiryYear,
  }) async{
    String baseUrl = productionMode ? 'https://api.openpay.mx' : 'https://sandbox-api.openpay.mx';

    String _merchantBaseUrl = '$baseUrl/v1/$merchantId';

    String basicAuth = 'Basic ' + base64Encode(utf8.encode('$publicApiKey:'));

    Response response = await post('$_merchantBaseUrl/tokens', headers: {
      'Content-type': 'application/json',
      'Authorization': basicAuth,
      'Accept': 'application/json',
    }, body: """{
      "card_number": "${cardNumber}",
      "holder_name": "${cardholderName}",
      "expiration_year": "${expiryYear}",
      "expiration_month": "${expiryMonth}",
      "cvv2": "${cvv}"
    }""");

    Map<String, dynamic> responseJson = json.decode(response.body);
    if (response.statusCode == 201) {
      String cardToken = responseJson["id"];
      return cardToken;
    } else {
      if(responseJson["error_code"] != null){
        throw Exception('Error ${responseJson["error_code"]}: ${responseJson["description"]}');
      }else{
        throw Exception('${response.body}');
      }
    }
  }

  /// Generates a device session id
  /// to use in Openpay API calls.
  ///
  /// Errors:
  ///   • `ERROR_UNABLE_TO_GET_SESSION_ID` - An error happened while generating the device session id.
  static Future<String> getDeviceSessionId({
    @required String merchantId,
    @required String publicApiKey,
    @required bool productionMode,
  }) {
    return _channel.invokeMethod('getDeviceSessionId', <String, dynamic>{
      'merchantId': merchantId,
      'publicApiKey': publicApiKey,
      'productionMode': productionMode,
    });
  }
}
