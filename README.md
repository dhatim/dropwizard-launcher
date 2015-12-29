# dropwizard-launcher
Maven plugin to start and stop dropwizard servers in a separate process.
This plugin is on maven central.

## USAGE

dropwizard-launcher-maven-plugin has two goals :
- launch-dropwizard : starts a dropwizard server
- stop-dropwizard : stops the dropwizard server started by launch-dropwizard

launch-dropwizard can take three parameters :
- jarFile : path to the dropwizard jar file
- jvmarg : options for the jvm
- confFile : the server YAML configuration file

stop-dropwizard has no parameter.

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
