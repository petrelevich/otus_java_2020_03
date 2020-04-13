package ru.otus.testing.example.services;

import java.io.InputStream;
import java.io.PrintStream;

public class ConsoleContext {
  private PrintStream out = System.out;
  private InputStream in = System.in;

  public ConsoleContext() {
  }

  public ConsoleContext(PrintStream out, InputStream in) {
    this.out = out;
    this.in = in;
  }

  public PrintStream getOut() {
    return out;
  }

  public InputStream getIn() {
    return in;
  }
}
