import 'package:fieldresearch/utils/utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

// ignore: must_be_immutable
class CustomTextField extends StatelessWidget {
  String textLabel = '';
  bool obscureText = false;
  bool? register = false;
  TextEditingController? controller;
  String? Function(String?)? validator;

  CustomTextField(
      {super.key,
      required this.textLabel,
      required this.obscureText,
      required this.validator,
      this.register,
      required this.controller});

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      validator: validator,
      controller: controller,
      obscureText: obscureText,
      cursorColor: Colors.white,
      cursorHeight: 28.h,
      style: TextStyle(color: Colors.white, fontSize: 14.sp),
      decoration: InputDecoration(
        contentPadding: EdgeInsets.symmetric(vertical: 15.h),
        filled: true,
        fillColor: fillFormColor,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(4),
          borderSide: BorderSide.none,
        ),
        hintText: textLabel,
        hintStyle: TextStyle(color: textColorForm, fontSize: 16.sp),
      ),
    );
  }
}
