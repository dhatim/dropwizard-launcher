# dropwizard-launcher
[![Build Status](https://travis-ci.org/dhatim/dropwizard-launcher.png?branch=master)](https://travis-ci.org/dhatim/dropwizard-launcher)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.dhatim/dropwizard-launcher/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.dhatim/dropwizard-launcher)
[![Javadocs](https://www.javadoc.io/badge/org.dhatim/dropwizard-launcher.svg)](https://www.javadoc.io/doc/org.dhatim/dropwizard-launcher)

Maven plugin to start and stop dropwizard servers in a separate process.

## USAGE

The dropwizard-launcher-maven-plugin has two goals:
- `launch-dropwizard`: starts a dropwizard server
- `stop-dropwizard`: stops the dropwizard server started by launch-dropwizard

Common configuration parameters:
- `stopPort` (default 44156): port used to notify the dropwizard server it must shut down. Use a projet-specific port to avoid conflicts. Under the hood this parameter sets the [`STOP.PORT` jetty parameter](https://wiki.eclipse.org/Jetty/Howto/Secure_Termination).
-  `skip` (default `false`): skip plugin execution.

Goal `launch-dropwizard` can take three parameters:
- `jarFile`: path to the dropwizard jar file
- `jvmarg`: options for the jvm
- `confFile`: the server YAML configuration file

Goal `stop-dropwizard` has no parameter.

## POM example :
```xml
<plugin>
  <groupId>org.dhatim</groupId>
  <artifactId>dropwizard-launcher-maven-plugin</artifactId>
  <version>1.1.0</version>
  <configuration>
      <stopPort>45678</stopPort>
      <skip>${skipITs}</skip>
  </configuration>
  <executions>
      <execution>
          <phase>integration-test</phase>
          <goals>
              <goal>launch-dropwizard</goal>
          </goals>
          <configuration>
              <jarFile>${project.build.directory}/${project.build.finalName}.jar</jarFile>
              <jvmarg>
              	<param>-javaagent:${project.build.directory}/jacoco-agent.jar=destfile=${project.build.directory}/coverage-reports/jacoco-it.exec,includes=*</param>
              </jvmarg>
              <confFile>${project.build.directory}/configuration.yml</confFile>
          </configuration>
      </execution>
      <execution>
          <phase>post-integration-test</phase>
          <goals>
              <goal>stop-dropwizard</goal>
          </goals>
      </execution>
  </executions>
</plugin>
```
