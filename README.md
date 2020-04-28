# flutter_openpay

A flutter plugin to tokenize cards using [Openpay](https://www.openpay.mx/)

[![pub package](https://img.shields.io/pub/v/flutter_openpay.svg)](https://pub.dartlang.org/packages/flutter_openpay)

## Installation

First, add _flutter_openpay_ as a dependency in [your pubspec.yaml file](https://flutter.io/platform-plugins/).

```
flutter_openpay: ^1.0.0
```

### Android

Add

```
<uses-permission android:name="android.permission.INTERNET"/>
```

before `<application>` to your app's `AndroidManifest.xml` file. This is required due to Openpay using its remote API to tokenize the card.

### iOS

For tokenizing the card information correctly, you need to add some keys to your iOS app's _Info.plist_ file, located in `<project root>/ios/Runner/Info.plist`:

- **_UIBackgroundModes_** with the **_fetch_** and **_remote-notifications_** keys - Required. Describe why your app needs to access background taks, suck talking to an external API (to tokenize the card). This is called _Required background modes_, with the keys _App download content from network_ and _App downloads content in response to push notifications_ respectively in the visual editor (since both methods aren't actually overriden, not adding this property/keys may only display a warning, but shouldn't prevent its correct usage).

  ```
  <key>UIBackgroundModes</key>
  <array>
     <string>fetch</string>
     <string>remote-notification</string>
  </array>
  ```

## Usage

There is only one method that should be used with this package:

#### `FlutterOpenpay.tokenizeCard()`

Will let you tokenize a card. This receives eight required parameters: the `publicApiKey` to specify your Openpay public key, the `productionMode` flag to indicate whether it should tokenize the card using the production or the sandbox API, the `merchantId` from Openpay, the `cardholderName`, the `cardNumber`, the `cvv`, the `expiryMonth` and the `expiryYear`. Returns a `String` with the token representing the card.

#### `FlutterOpenpay.getDeviceSessionId()`

Will let you generate a unique device session id, which you should use to access resources from the [Openpay API](https://www.openpay.mx/docs/api/). This receives three required parameters: the `productionMode` flag, the `publicApiKey` to specify your Openpay public key, and the `merchantId` from Openpay. Returns a `String` with the device session id.

## Currently supported features

- [x] Tokenize card using **Openpay**.
- [x] Generate device session id to access [Openpay API](https://www.openpay.mx/docs/api/) resources.

## Demo App

![Demo](https://github.com/alfredoqt/flutter_openpay/blob/master/example/example.png)

## Example

See example app.
