# jinx

A Clojure library designed to explore Leiningen test selectors. See
project.clj for example selectors.

> J.K. Rowling defined jinxes as "spells whose effects are irritating but amusing."

A sample run:

```bash
# --------------------------------------------------------------
# Run all with the default selector (complement :integration)
# --------------------------------------------------------------
mourjo@andromeda:$ time lein test
*** [SRC] Loading jinx.util ***
*** [SRC] Loading jinx.core ***
*** [TEST] Loading jinx.core-test ***
*** [TEST] Loading jinx.util-test ***

lein test jinx.core-test
Running system-timezones-test

lein test jinx.util-test

Ran 1 tests containing 7 assertions.
0 failures, 0 errors.

real	0m6.541s
user	0m16.459s
sys	0m0.695s


# --------------------------------------------------------------
# Run all integration tests
# --------------------------------------------------------------
mourjo@andromeda:$ time lein test :integration
*** [SRC] Loading jinx.util ***
*** [SRC] Loading jinx.core ***
*** [TEST] Loading jinx.core-test ***
*** [TEST] Loading jinx.util-test ***

lein test jinx.core-test
Running remote-timezones-test
Running remote-api-info-test
Running remote-system-time-difference-test
Running estimate-remote-time-error-test

lein test jinx.util-test
Running fetch-test

Ran 5 tests containing 22 assertions.
0 failures, 0 errors.

real	0m55.866s
user	0m18.965s
sys	0m0.856s


# --------------------------------------------------------------
# Run only integration tests in a namespace
# --------------------------------------------------------------
mourjo@andromeda:$ time lein test :integration jinx.util-test
*** [SRC] Loading jinx.util ***
*** [TEST] Loading jinx.util-test ***

lein test jinx.util-test
Running fetch-test

Ran 1 tests containing 3 assertions.
0 failures, 0 errors.

real	0m15.870s
user	0m16.685s
sys	0m0.707s


# --------------------------------------------------------------
# Run only integration tests in a namespace like above
# but use the integration-simple selector (all namespaces
# will be loaded, even if not necessary)
# --------------------------------------------------------------
mourjo@andromeda:$ time lein test :integration-simple jinx.util-test
*** [SRC] Loading jinx.util ***
*** [SRC] Loading jinx.core ***
*** [TEST] Loading jinx.core-test ***
*** [TEST] Loading jinx.util-test ***

lein test jinx.util-test
Running fetch-test

Ran 1 tests containing 3 assertions.
0 failures, 0 errors.

real	0m15.984s
user	0m17.148s
sys	0m0.751s


# --------------------------------------------------------------
# Run integration tests in a namespace which does not exist
# --------------------------------------------------------------
mourjo@andromeda:$ time lein test :integration DOES-NOT-EXIST

lein test user

Ran 0 tests containing 0 assertions.
0 failures, 0 errors.

real	0m3.773s
user	0m8.667s
sys	0m0.453s


# --------------------------------------------------------------
# Run integration tests in a namespace which does not exist
# using the integration-simple selector (existing namespaces
# will be loaded even though they are not required)
# --------------------------------------------------------------
mourjo@andromeda:$ time lein test :integration-simple DOES-NOT-EXIST
*** [SRC] Loading jinx.util ***
*** [SRC] Loading jinx.core ***
*** [TEST] Loading jinx.core-test ***
*** [TEST] Loading jinx.util-test ***

lein test user

Ran 0 tests containing 0 assertions.
0 failures, 0 errors.

real	0m6.090s
user	0m15.768s
sys	0m0.668s


# --------------------------------------------------------------
# Run integration tests from a given namespace and another
# test var
# --------------------------------------------------------------
mourjo@andromeda:$ time lein test :integration jinx.util-test jinx.core-test/remote-system-time-difference-test
*** [SRC] Loading jinx.util ***
*** [SRC] Loading jinx.core ***
*** [TEST] Loading jinx.core-test ***
*** [TEST] Loading jinx.util-test ***

lein test jinx.core-test
Running remote-system-time-difference-test

lein test jinx.util-test
Running fetch-test

Ran 2 tests containing 10 assertions.
0 failures, 0 errors.

real	0m24.050s
user	0m17.581s
sys	0m0.744s
```



## License

Copyright © 2020 Mourjo Sen

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
