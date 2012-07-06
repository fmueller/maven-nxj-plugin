package no.heim.maven.plugins.nxj;

import java.io.File;
import java.io.IOException;

import js.tinyvm.TinyVMException;
import lejos.pc.tools.NXJLink;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Echos an object string to the output screen.
 * @goal link
 * @requiresProject true
 * @execute phase="compile"
 */
public class LinkMojo extends AbstractMojo {

	/**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Location of lejos-classes.
     * @parameter expression="${bootClassPath}"
     */
    private Object bootClassPath;
    
    /**
     * Name of main class
     * @parameter expression="${mainClass}"
     */
    private Object mainClass;
    
    /**
     * Name of output application
     * @parameter expression="${applicationName}
     */
    private Object applicationName;

    public void execute() throws MojoExecutionException, MojoFailureException {
    	final ClasspathBuilder cpBuilder = new ClasspathBuilder(
    			System.getProperty("user.home") + File.separator + ".m2" + File.separator + "repository",
    			project.getDependencies());
    	try {
			new NXJLink().run(
					new String[] { "-bp", bootClassPath.toString(),
								   "-cp", "target" + File.separator +"classes" + File.pathSeparator + cpBuilder.build(),
								   "-wo", "LE", "-o", "target" + File.separator + applicationName.toString(),
								   mainClass.toString() });
		} catch (TinyVMException e) {
			getLog().error("Could not perform linking", e);
			throw new MojoFailureException(e, "Error", "");
		} catch (IOException e) {
			getLog().error("Could not perform linking", e);
			throw new MojoFailureException(e, "Error", "");
		}
    }
}
