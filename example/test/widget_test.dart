import 'package:flutter_test/flutter_test.dart';

import 'package:flutter_carrotquest_example/main.dart';

void main() {
  testWidgets('Verify Platform version', (WidgetTester tester) async {
    await tester.pumpWidget(MyApp());
  });
}
