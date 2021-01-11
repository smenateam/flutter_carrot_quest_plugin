#import "FlutterCarrotQuestPlugin.h"
#if __has_include(<flutter_carrotquest/flutter_carrotquest-Swift.h>)
#import <flutter_carrotquest/flutter_carrotquest-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_carrotquest-Swift.h"
#endif

@implementation FlutterCarrotQuestPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterCarrotQuestPlugin registerWithRegistrar:registrar];
}
@end
