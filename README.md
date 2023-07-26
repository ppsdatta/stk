# stk

A Clojure library that enables you to code like it is Forth

## Usage

In Forth everything is evaluated on a global Stack. What if we had the same ability in Clojure - not a separate language but within Clojure as a library.

Example:
```[clojure]
[{:double [dup plus]} 1 2 2 plus :double :double mult dup minus .]
```


## License

Copyright © 2022 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
