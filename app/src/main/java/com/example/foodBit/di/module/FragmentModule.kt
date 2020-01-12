package com.example.foodBit.di.module

import com.example.foodBit.ui.ListFragment
import com.example.foodBit.ui.DetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeListFragment(): ListFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment
}