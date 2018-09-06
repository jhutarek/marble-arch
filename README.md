# marble-arch
TODO

## Design notes - MVVM

* Do not hide `Observable`s and other reactive types for more flexibility (user of the API might want to modify the stream before subscribing with our methods).
* In lambdas, `it` should be "small" event or value, `this` should be "big" receiver like ViewModel.
