package cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CucumberStep {
  List<Integer> resultList;
  Integer size;

  @Given("Empty Array List")
  public void emptyList() {
    resultList = new ArrayList<>();
  }

  @When("Generate A Random Digit Object In List {int} times")
  public void genRandomDigit(int times) {
    for (int i = 0; i < times; i++) {
      Integer r = new Random().nextInt(10);
      System.out.printf("digit --> %d%n", r);
      if (r % 2 == 0) {
        resultList.add(r);
      }
    }
  }

  @And("Check Array List Size")
  public void checkSize() {
    size = resultList.size();
  }

  @Then("Show Result")
  public void showResult() {
    System.out.printf(
        size == 0
            ? "result list is empty."
            : "result list size: %d", size
    );
  }
}
