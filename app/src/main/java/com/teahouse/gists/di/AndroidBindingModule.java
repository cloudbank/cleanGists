package com.teahouse.gists.di;

import com.teahouse.gists.ui.GistActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Binds all activity sub-components within the app.
 */
@Module
abstract class AndroidBindingModule {
  //@todo custom activity scope
  @ContributesAndroidInjector
  abstract GistActivity contributesGistActivity();
}