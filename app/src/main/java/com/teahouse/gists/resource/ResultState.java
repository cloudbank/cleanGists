package com.teahouse.gists.resource;

public class ResultState<T, Throwable> {
  public Throwable error;
  public T data;

  public ResultState(T data, Throwable error) {
    this.error = error;
    this.data = data;
  }
}
