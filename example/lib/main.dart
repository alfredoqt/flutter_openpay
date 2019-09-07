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

  @override
  void initState() {
    super.initState();
  }

  String validateCardNameField(String value) {
    if (value.length == 0) {
      return "Por favor escribe el nombre";
    }
    if (!(value.contains(new RegExp("^[^0-9]+\$")))) {
      return "El nombre no puede incluír números";
    }
    return null;
  }

  String validateCardNumberField(String value) {
    if (value.length != 16) {
      return "Se necesitan los 16 números";
    }
    return null;
  }

  String validateExpMonthField(String value) {
    if (value.length != 2) {
      return "Escribe el més en el formato MM";
    }

    if (int.tryParse(value) < 1 || int.tryParse(value) > 12) {
      return "$value no es un mes válido";
    }

    //Validar de acuerdo a la fecha que resulta de mes y año
    return null;
  }

  String validateExpYearField(String value) {
    if (value.length != 4) {
      return "Escribe el año en el formato YYYY";
    }

    //Validar de acuerdo a la fecha que resulta de mes y año
    return null;
  }

  String validateBackNumberField(String value) {
    if (value.length != 3) {
      return "Se necesitan los tres números detrás de tu tarjeta";
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

  Widget cardNameField() {
    return Container(
      child: TextFormField(
        autofocus: true,
        keyboardType: TextInputType.text,
        enabled: true,
        decoration: InputDecoration(
            labelText: "Nombre del titular", border: OutlineInputBorder()),
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
            labelText: "Número de tarjeta", border: OutlineInputBorder()),
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
        maxLength: 4,
        keyboardType: TextInputType.number,
        enabled: true,
        decoration:
            InputDecoration(labelText: "YYYY", border: OutlineInputBorder()),
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
            InputDecoration(labelText: "CVC", border: OutlineInputBorder()),
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
          'Registrar tarjeta',
          style: new TextStyle(color: Colors.white),
        ),
        onPressed: () => this.submit(),
        color: Colors.blue,
      ),
      margin: new EdgeInsets.only(top: 20.0),
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(),
      title: "Conekta Tokenization Example",
      home: Scaffold(
        appBar: AppBar(
          title: Text("Conekta Tokenization Example"),
        ),
        body: Container(
          padding: EdgeInsets.all(20),
          child: new Form(
            key: _formKey,
            child: new ListView(
              children: <Widget>[
                SizedBox(height: 20),
                cardNameField(),
                SizedBox(height: 20),
                cardNumberField(),
                cardValidationRow(),
                tokenizeCardButton(),
                SizedBox(height: 20),
                Text("Token: $_token")
              ],
            ),
          ),
        ),
      ),
    );
  }
}
