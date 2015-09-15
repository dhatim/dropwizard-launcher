# dropwizard-launcher
Maven plugin to start and stop dropwizard servers in a separate process

USAGE
dropwizard-launcher-maven-plugin has two goals :
- launch-dropwizard : allows to start dropwizard server
- stop-dropwizard : allows to stop dropwizard server

launch-dropwizard goal has three different parameters :
- jarFile : is the dropwizard jar file
- jvmarg : is a list of options for the jvm
- confFile : is a YAML configuration file

stop-dropwizard goal has no parameters and can stop the previous dropwizard server started by launch-dropwizard goal.
