#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint flutter_carrotquest.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'flutter_carrotquest'
  s.version          = '1.0.1'
  s.summary          = 'A Carrot quest flutter plugin for Android and IOS. You can find a description of the service at https://www.carrotquest.io'
  s.description      = <<-DESC
  A Carrot quest flutter plugin for Android and IOS. You can find a description of the service at https://www.carrotquest.io
                       DESC
  s.homepage         = 'https://dipdev.studio'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'DipDev Studio' => 'info@dipdev.studio' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.platform = :ios, '10.0'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
  s.dependency 'CarrotquestSDK'
end
