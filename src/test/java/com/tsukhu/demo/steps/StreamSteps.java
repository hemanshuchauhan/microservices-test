package com.tsukhu.demo.steps;

import com.tsukhu.demo.domain.Employee;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Ignore;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@Ignore
public class StreamSteps {

    private IntStream numStream;
    private List<Employee> employeeList;

    @Given("a stream of integers (.*)")
    public void a_stream_of_integers(String numbers){
        this.numStream = Arrays.stream(numbers.substring(1, numbers.length()-1).split(","))
                .map(String::trim).mapToInt(Integer::parseInt);

    }

    @Then("the minimum is (.*)")
    public void the_minimum_is(int min) {
        numStream.min()
                .ifPresent(val -> assertEquals(val,min) );
    }

    @Then("the sorted three distinct numbers are (.*)")
    public void the_sorted_three_distinct_numbers_are(String numbers) {
        String expectedOutput = removeSquareBracketsFromList(numbers);
        String actualOutput = numStream.distinct()
                .sorted()
                .limit(3)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(","));
        assertEquals(expectedOutput,actualOutput);
    }

    @Given("a list of employees$")
    public void a_list_of_employees(List<Employee> employees) {
        this.employeeList = employees;
    }

    @Then("the top three earning employees are (.*)")
    public void the_top_three_earning_employees_are(String employees){
        String expectedOutput = removeSquareBracketsFromList(employees);
        // Compare employee list , sorted , compare salary and limit to 3
        String actualOutput = employeeList.stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                .limit(3)
                .map(Employee::getName)
                .collect(Collectors.joining(","));
        assertEquals(expectedOutput,actualOutput);
    }

    @Then("the top three active earning employees are (.*)")
    public void the_top_three_active_earning_employees_are(String employees){
        String expectedOutput = removeSquareBracketsFromList(employees);
        // Compare employee list , sorted , compare salary and limit to 3
     //   System.out.println(expectedOutput);
        String actualOutput = employeeList.stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                .filter(employee -> employee.getIsActive())
                .limit(3)
                .map(Employee::getName)
                .collect(Collectors.joining(","));
        assertEquals(expectedOutput,actualOutput);
    }

    @Then("^the top active earning employees per department")
    public void the_top_active_earning_employees_per_department(Map<Integer,String> expectedMap) {
        String expected = expectedMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, String>comparingByKey())
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(","));

        Map<Integer,String> topEarners = new HashMap<Integer, String>();

        employeeList.stream()
                        .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                        .filter(employee -> employee.getIsActive())
                        .collect(Collectors.groupingBy(e -> e.getDepartmentId()))
                        .forEach((id, employeeList) -> {
                         String employeeName = employeeList.stream().sorted(Comparator.comparingInt(Employee::getSalary).reversed()).limit(1).map(Employee::getName).collect(Collectors.joining());
                         topEarners.put(id,employeeName);
        });

        String actual = topEarners.entrySet().stream()
                .sorted(Map.Entry.<Integer, String>comparingByKey())
                .map(entry -> entry.getKey().toString() + " - " + entry.getValue())
                .collect(Collectors.joining(","));

        assertEquals(expected,actual);
    }

    @Then("the active employee count per department")
    public void the_active_count_employees_per_department(Map<Integer,Integer> expectedMap) {
        String expected = expectedMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByKey())
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(","));

        Map<Integer, Long> employeeCount = employeeList
                .stream()
                .filter(employee -> employee.getIsActive())
                .collect(Collectors.groupingBy(e -> e.getDepartmentId(), Collectors.counting()));

        String actual = employeeCount.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByKey())
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(","));
        assertEquals(expected,actual);
    }

    /**
     * Converts the array represented as a string to a comma separate output
     * @param inputString
     * @return
     */
    private String removeSquareBracketsFromList(String inputString) {
        return inputString.substring(1, inputString.length() - 1);
    }
}
