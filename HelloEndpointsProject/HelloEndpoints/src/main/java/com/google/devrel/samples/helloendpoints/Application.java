package com.google.devrel.samples.helloendpoints;

import com.google.api.client.util.Lists;
import com.google.api.services.helloworld.model.Greeting;

import java.util.ArrayList;

/**
 * Dummy Application class that can hold static data for use only in sample applications.
 *
 * TODO(developer): Implement a proper data storage technique for your application.
 */
public class Application extends android.app.Application {
  ArrayList<Greeting> greetings = Lists.newArrayList();
}
