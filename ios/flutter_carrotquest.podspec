require 'yaml'

pubspec = YAML.load_file(File.join('..', 'pubspec.yaml'))
library_version = pubspec['version'].gsub('+', '-')

#if defined?($FirebaseSDKVersion)
#  Pod::UI.puts "#{pubspec['name']}: Using user specified Firebase SDK version '#{$FirebaseSDKVersion}'"
#  firebase_sdk_version = $FirebaseSDKVersion
#else
#  firebase_core_script = File.join(File.expand_path('..', File.expand_path('..', File.dirname(__FILE__))), 'firebase_core/ios/firebase_sdk_version.rb')
#  if File.exist?(firebase_core_script)
#    require firebase_core_script
#    firebase_sdk_version = firebase_sdk_version!
#    Pod::UI.puts "#{pubspec['name']}: Using Firebase SDK version '#{firebase_sdk_version}' defined in 'firebase_core'"
#  end
#end

Pod::Spec.new do |s|
  s.name             = pubspec['name']
  s.version          = library_version
  s.summary          = pubspec['description']
  s.description      = pubspec['description']
  s.homepage         = pubspec['homepage']
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'DipDev Studio' => 'info@dipdev.studio' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'

  #s.ios.deployment_target = '10.0'

  s.dependency 'Flutter'
  s.platform = :ios, '10.0'

  #s.dependency 'firebase_core'
  #s.dependency 'Firebase/Messaging', firebase_sdk_version

  s.dependency 'CarrotquestSDK'
  
  #s.static_framework = true
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
end