# dropwizard-launcher
Maven plugin to start and stop dropwizard servers in a separate process
This plugin is on maven central.
The server is launched in a separate jvm, so it is easy get integration test coverage.

## USAGE

dropwizard-launcher-maven-plugin has two goals :
- launch-dropwizard : allows to start dropwizard server
- stop-dropwizard : allows to stop dropwizard server

launch-dropwizard goal has three different parameters :
- jarFile : is the dropwizard jar file
- jvmarg : is a list of options for the jvm
- confFile : is a YAML configuration file

stop-dropwizard goal has no parameters and can stop the previous dropwizard server started by launch-dropwizard goal.

## POM example :
```xml
<plugin>
  <groupId>org.dhatim</groupId>
  <artifactId>dropwizard-launcher-maven-plugin</artifactId>
  <version>1.0.0</version>
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
