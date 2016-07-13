ScriptedPlugin.scriptedSettings

scriptedLaunchOpts := scriptedLaunchOpts.value ++ Vector(
  "-Xmx1024M",
  s"-Dplugin.version=${version.value}"
)

scriptedBufferLog := false
