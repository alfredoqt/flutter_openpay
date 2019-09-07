import 'dart:async';

import 'package:flutter/services.dart';

class FlutterOpenpay {
  static const MethodChannel _channel =
      const MethodChannel('flutter_openpay');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
