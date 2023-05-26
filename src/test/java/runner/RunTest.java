package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"/Users/chun/project/tester/rpa/src/test/resources/cucumber/cucumber.feature"},
    glue = {"cucumber"},
    plugin = {"pretty"}
)
public class RunTest {

}
