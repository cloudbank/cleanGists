package com.teahouse.gists.resource;

import static com.teahouse.gists.resource.Status.ERROR;
import static com.teahouse.gists.resource.Status.LOADING;
import static com.teahouse.gists.resource.Status.SUCCESS;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class Resource<T> {
  @NonNull
  public final Status status;
  @Nullable
  public final T data;
  @Nullable
  public final String message;

  private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
    this.status = status;
    this.data = data;
    this.message = message;
  }

  public static <T> Resource<T> success(@NonNull T data) {
    return new Resource<>(SUCCESS, data, null);
  }

  public static <T> Resource<T> error(String msg, @Nullable T data) {
    return new Resource<>(ERROR, data, msg);
  }

  public static <T> Resource<T> loading(@Nullable T data) {
    return new Resource<>(LOADING, data, null);
  }
}
