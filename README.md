# utils

Some utilities to make Java 9 even more expressive. This will be updated with code examples in the near future

#### Try
Try is a java version of the Scala type.


#### ForComp
ForComp attempts to give Java a little of the power of for comprehension similar to that of Scala.

Obviously, without the language having for comprehension built in there must be some compromises, but the result of the 
for comprehension here is still a powerful tool for Java.

The test cases give some good examples of usage

An example of swapping a nest loop for for comp

```
    private int nestedLoops() {
        int result = 0;

        for (int i = 0; i < 100; i ++) {
            for(int j = 0; j < 50; j++) {
                for(int k = 0; k < 20; k++) {
                    result += (i + j + k);
                }
            }
        }

        return result;
    }

    private int nestedForComp() {
        return forComp(IntStream.range(0, 100).boxed()).
                with(IntStream.range(0, 50).boxed()).
                with(IntStream.range(0, 20).boxed(), forFunction(l -> l.get(2) + l.get(1) + l.get(0), 0, 1)).
                yield().mapToInt(value -> (Integer) value).sum();
    }
    
```

#### Match
Match gives a similar pattern matching function to that of Scala's match. <code>Match.match</code> takes a subject to 
match on and then a number of <code>Case</code> types for the patterns.

Like Scala's match if no pattern matches then a MatchException will be thrown

The <code>Case</code> class takes a predicate and a function that will be called if matched.

There are three helper methods in the <code>Match</code> class to help create <code>Case</code> types:
<code>matchCase</code> creates a <code>Case</code> based on the predicate and function supplied
<code>typeCase</code> takes a <code>Class</code> and matched based on type comparison
<code>defaultCase</code> only takes a function and is used as a fall through match

Also like Scala's match, order is important and the match function will complete on the first pattern matched 
