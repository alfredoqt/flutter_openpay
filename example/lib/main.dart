import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_openpay/flutter_openpay.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _formKey = GlobalKey<FormState>();

  String _name = "";
  String _number = "";
  String _expMonth = "";
  String _expYear = "";
  String _cvc = "";
  String _token = "";
  String _deviceSessionId = "";

  @override
  void initState() {
    super.initState();
  }

  String validateCardNameField(String value) {
    if (value.length == 0) {
      return "Please write your name";
    }
    return null;
  }

  String validateCardNumberField(String value) {
    return null;
  }

  String validateExpMonthField(String value) {
    if (value.length != 2) {
      return "Write like MM";
    }

    if (int.tryParse(value) < 1 || int.tryParse(value) > 12) {
      return "$value not valid";
    }

    //Validar de acuerdo a la fecha que resulta de mes y año
    return null;
  }

  String validateExpYearField(String value) {
    if (value.length != 2) {
      return "Write like YY";
    }

    //Validar de acuerdo a la fecha que resulta de mes y año
    return null;
  }

  String validateBackNumberField(String value) {
    if (value.length != 3) {
      return "Three digits";
    }
    return null;
  }

  Future<void> submit() async {
    if (_formKey.currentState.validate()) {
      _formKey.currentState.save();

      String token;

      try {
        token = await FlutterOpenpay.tokenizeCard(
          cardholderName: _name,
          cardNumber: _number,
          cvv: _cvc,
          expiryMonth: _expMonth,
          expiryYear: _expYear,
          publicApiKey: 'pk_3a66482b770a4d1abd5bf21aaf01dffb',
          merchantId: 'mop7w9rqjzbkhcmwcoob',
          productionMode: false,
        );
      } catch (e) {
        print(e.toString());
        token = "Unable to tokenize card";
      }

      setState(() {
        _token = token;
      });
    }
  }

  Future<void> requestDeviceSessionId() async {
    String deviceSessionId;
    try {
      deviceSessionId = await FlutterOpenpay.getDeviceSessionId(
        merchantId: 'm3ts0hkttkzst0ygk8ha',
        publicApiKey: 'pk_3a6c4e052213416c84c9f2da10bcb7d2',
        productionMode: false,
      );
      setState(() {
        _deviceSessionId = deviceSessionId;
      });
    } catch (e) {
      print(e.toString());
      deviceSessionId = "Unable to tokenize card";
      setState(() {
        _deviceSessionId = deviceSessionId;
      });
    }
  }

  Widget cardNameField() {
    return Container(
      child: TextFormField(
        autofocus: true,
        keyboardType: TextInputType.text,
        enabled: true,
        decoration: InputDecoration(
            labelText: "Holder name", border: OutlineInputBorder()),
        onSaved: (String value) {
          _name = value;
        },
        validator: validateCardNameField,
      ),
    );
  }

  Widget cardNumberField() {
    return Container(
      child: TextFormField(
        keyboardType: TextInputType.number,
        maxLength: 16,
        enabled: true,
        decoration: InputDecoration(
            labelText: "Card number", border: OutlineInputBorder()),
        onSaved: (String value) {
          _number = value;
        },
        validator: validateCardNumberField,
      ),
    );
  }

  Widget cardExpirationMonthField() {
    return Expanded(
      flex: 2,
      child: TextFormField(
        maxLength: 2,
        keyboardType: TextInputType.number,
        enabled: true,
        decoration:
            InputDecoration(labelText: "MM", border: OutlineInputBorder()),
        onSaved: (String value) {
          _expMonth = value;
        },
        validator: validateExpMonthField,
      ),
    );
  }

  Widget cardExpirationYearField() {
    return Expanded(
      flex: 4,
      child: TextFormField(
        maxLength: 2,
        keyboardType: TextInputType.number,
        enabled: true,
        decoration:
            InputDecoration(labelText: "YY", border: OutlineInputBorder()),
        onSaved: (String value) {
          _expYear = value;
        },
        validator: validateExpYearField,
      ),
    );
  }

  Widget cardBackNumberField() {
    return Expanded(
      flex: 3,
      child: TextFormField(
        maxLength: 3,
        keyboardType: TextInputType.number,
        enabled: true,
        decoration:
            InputDecoration(labelText: "CVV", border: OutlineInputBorder()),
        onSaved: (String value) {
          _cvc = value;
        },
        validator: validateBackNumberField,
      ),
    );
  }

  Widget cardValidationRow() {
    return Row(
      children: <Widget>[
        cardExpirationMonthField(),
        SizedBox(width: 20),
        cardExpirationYearField(),
        SizedBox(width: 20),
        cardBackNumberField(),
      ],
    );
  }

  Widget tokenizeCardButton() {
    return new Container(
      width: 240,
      height: 48,
      child: new RaisedButton(
        child: new Text(
          'Register card',
          style: new TextStyle(color: Colors.white),
        ),
        onPressed: () => this.submit(),
        color: Colors.blue,
      ),
      margin: new EdgeInsets.only(top: 16.0),
    );
  }

  Widget deviceSessionIdButton() {
    return new Container(
      width: 240,
      height: 48,
      child: new RaisedButton(
        child: new Text(
          'Get device session id',
          style: new TextStyle(color: Colors.white),
        ),
        onPressed: requestDeviceSessionId,
        color: Colors.blue,
      ),
      margin: new EdgeInsets.only(top: 16.0),
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(),
      title: "Openpay Tokenization Example",
      home: Scaffold(
        appBar: AppBar(
          title: Text("Openpay Tokenization Example"),
        ),
        body: SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.all(16),
            child: new Form(
              key: _formKey,
              child: new Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: <Widget>[
                  SizedBox(height: 16),
                  cardNameField(),
                  SizedBox(height: 16),
                  cardNumberField(),
                  cardValidationRow(),
                  tokenizeCardButton(),
                  SizedBox(height: 16),
                  Text("Token: $_token"),
                  deviceSessionIdButton(),
                  SizedBox(height: 16),
                  Text("Device session id: $_deviceSessionId"),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
