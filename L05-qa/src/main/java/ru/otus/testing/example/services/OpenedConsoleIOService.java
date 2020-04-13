package ru.otus.testing.example.services;

import java.io.PrintStream;
import java.util.Scanner;

public class OpenedConsoleIOService implements IOService {
  private final PrintStream out;
  private final Scanner sc;


  public OpenedConsoleIOService(ConsoleContext ctx) {
    this.out = ctx.getOut();
    this.sc = new Scanner(ctx.getIn());
  }

  @Override
  public void out(String message) {
    out.println(message);
  }

  @Override
  public String readString() {
    return sc.nextLine();
  }
}
