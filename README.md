# flutter_carrotquest

A Carrot quest flutter plugin for Android and IOS.

## Getting Started

```dart
import 'package:flutter_carrotquest/flutter_carrotquest.dart';
```

```dart
await Carrot.setup(apiKey: 'apiKey', appId: 'appId')
        .catchError((onError) => print(onError));
```

```dart
await Carrot.openChat();
```

## Features and bugs

Please file feature requests and bugs at the [issue tracker][tracker].

[tracker]: https://gitlab.com/dipdev.studio/open-source/flutter/flutter_carrotquest/-/issues
