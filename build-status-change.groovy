// https://mvnrepository.com/artifact/org.jenkins-ci.main/jenkins-core
import jenkins.model.*
import hudson.model.*
import java.util.regex.*
import groovy.transform.Field
import javax.mail.internet.*

logger.println()
logger.println '@' + this.class.getName() + ' begins'

@Field final takeLastLogLines = 250


//logger.println "\n\n\n\n\n"
//logger.println build.properties.collect{it}.join('\n\n\n')
//logger.println build.project.name
//logger.println build.properties.envVars.collect{it}.join('\n\n\n')
//logger.println build.properties.environment.collect{it}.join('\n\n\n')
//logger.println "\n\n\n\n\n"




def logPart = ''
if(build.result == Result.FAILURE) {
    def l = build.log
    def reversLastRows = l
        .split('\n')
        .reverse()
        .take(takeLastLogLines)
        .join('\n')


    logPart = """
<br/>
<div>Last <b>${takeLastLogLines}</b> lines of build log in the <span style="color: red;">reverse</span> order</div>
<pre style="color: grey">
${reversLastRows}
</pre>
<br/>
<div><b>...</b></div>
"""
}

String failStyle = ''
if(build.result == Result.FAILURE){
    failStyle = 'style="color: red"'
}
if(build.result == Result.SUCCESS){
    failStyle = 'style="color: green"'
}

// Bug: https://stackoverflow.com/questions/17262161/how-to-modify-the-mime-message-in-editable-email-plugin-in-jenkins/49126212#49126212
String content = """
<h3>
    <div ${failStyle}>$build.result</div>
</h3>
<div>Project: <b>$build.project.name</b></div>
<br/>
<div>Build: <b>$build.displayName</b></div>
<br/>
See the full output at <a href="${build.absoluteUrl}console">Capital</a>.<br/>

${logPart}
"""
msg.setContent(content, 'text/html')






logger.println '@' + this.class.getName() + ' ends'
logger.println()
