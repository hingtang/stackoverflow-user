package com.hing.stackoverflowuser.testing

/**
 * Created by HingTang on 2019-06-08.
 */

/**
 * This annotation allows us to open some classes for mocking purposes while they are final in
 * release builds.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class OpenClass

/**
 * Annotate a class with [OpenForTesting] if you want it to be extendable in debug builds.
 */
@OpenClass
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting
