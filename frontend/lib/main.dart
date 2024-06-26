import 'package:app_client/app_client.dart';
import 'package:fieldresearch/app/app.dart';
import 'package:fieldresearch/app/simple_bloc_observer.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:research_repository/research_repository.dart';
import 'package:user_repository/user_repository.dart';

void main() async {
  Bloc.observer = SimpleBlocObserver();
  runApp(
    MainApp(
      userRepository: SpringUserRepository(client: HttpClient()),
      researchRepository: SpringResearchRepository(
        client: HttpClient(),
      ),
    ),
  );
}
