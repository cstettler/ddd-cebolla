package com.github.cstettler.cebolla.sample.application.test;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class RegexpMatcher extends TypeSafeMatcher<String> {

  private final Pattern regexp;

  private RegexpMatcher(String regexp) {
    this.regexp = compile(regexp);
  }

  @Override
  protected boolean matchesSafely(String value) {
    return this.regexp.matcher(value).matches();
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("matches regexp '" + this.regexp.pattern() + "'");
  }

  public static RegexpMatcher matches(String regexp) {
    return new RegexpMatcher(regexp);
  }

}
