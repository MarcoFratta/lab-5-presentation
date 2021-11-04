## TODO

### Example 0

1. Have a look into the `src/` folder

> __Goal__: here a very trivial example of (de)serialization with Gson is provided


### Exercise 1

1. Consider the `calc-*` subprojects, providing a notion of _calculator_ and its local and remote implementation
   - `:calc-core` exposes a `Calcultator` interface and its local implementation
       * a `Calculator` simply allows callers to perform basic algebraic operations (sum, difference, product, and division) between two or more numbers
   - `:calc-client` and `:calc-server` provide a remote implementation for the `Calculator` interface, realising the client- and server-side, respectively
     *  they essentially exploit the RPC pattern to support such remote implementation
   - `:calc-test` provides unit tests for the above
   - Their dependencies are provided below:

       ![`calc-*` dependencies](http://www.plantuml.com/plantuml/svg/SoWkIImgAStDuKhEpot8pqlDAr5GiafEp4dM1GhavkSfAIGMAq2Oc4gAKulAKel0IY1CBARaP09vO2KG9gQIajHYY08GWuiBOfc2JOskBjnKX13C50W55eBeWoX54d18pKi1UWm0)
   
2. The remote implementation attempts to highlight the client and server stubs of a RPC solution aimed at performing basic calculations
    - A custom transport protocol is used for data exchange, implemented via Sockets
    - Data presentation is custom as well, and tailored on Java's data model

3. Look for the many placeholders in these project and fill them, in order to make the provided tests pass

> __Goal__: realise a simple RPC service and the client to use it, while having a taste of the intricacies of custom presentation


### Exercise 2 _(mandatory)_

1. Consider the `auth-*` subprojects, providing a notion of _authenticator_, and its local and remote implementation 
    - `:auth-core` exposes a `Authenticator` interface and its local implementation
        * an `Authenticator` is a simple service supporting the _registration_ of `User`s, as well as their _authorization_
            + __registration__ accepts a `User` description as input, and can either succeed with no result or fail in case of _conflict_ or _badly_ formatted request
            + __authorization__ accepts some user's `Credentials` as input, and can either succeed returning a `Token` or fail in case of `wrong` credentials or _badly_ formatted request
    - `:auth-client` and `:auth-server` provide a remote implementation for the `Authenticator` interface, realising the client- and server-side, respectively
        * they essentially exploit the RPC pattern to support such remote implementation
    - `:auth-presentation` provides (de)serialization facilities to be used by both `:auth-client` and `:auth-server`
    - `:auth-test` provides unit tests for the above
    - Their dependencies are provided below:
    
        ![`auth-*` dependencies](http://www.plantuml.com/plantuml/svg/POz1peKW38JtdeAunmCOOxmAqGPDW1fQ__oBT_W7r0KltP3fp9SEtOXTvYU6OSTuOuB4PfpTmwSjHiGq6aT6f4Rk35H6nzSBzaQF5pbvH1zWsl60oHEckJwZMHRuPyZ9XNMjAwghcmZo7-JM8L8ZUxZAr12H6c4W0QeO1ZdBB1zhVt_U3n9BgGxUAXNaovzivHX16Yx5igmpDaZ74yCN)

2. The remote implementation attempts to highlight the client and server stubs of a RPC solution aimed at performing basic calculations
    - A custom transport protocol is used for data exchange, implemented via Sockets
    - Data presentation is based on the __JSON__ data format, supported by the [`Gson`](https://github.com/google/gson) library

3. Look for the many placeholders in these project and fill them, in order to make the provided tests pass
    - Also, take care of realising the necessary serializers and deserializers, for all the data structures involved
    - Feel free to add new data structures if you believe its the case

> __Goal__: realise a simple RPC service and the client to use it, while understanding the benefits and the intricacies of standardadised presentation
