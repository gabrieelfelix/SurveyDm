import 'package:app_client/src/client_repo.dart';
import 'package:http/http.dart' as http;

class HttpClient implements IClientHttp {
  final client = http.Client();

  @override
  Future post({required String url, required String body}) async {
    final response = await client.post(
      Uri.parse(url),
      headers: {
        'Content-Type': 'application/json',
      },
      body: body,
    );

    return response;
  }

  @override
  Future get({required String url}) async {
    final response = await client.get(Uri.parse(url));
    return response;
  }
}
