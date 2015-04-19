## Peek Problems

This small snippet of code demonstrates some wrong use of peek method in Java 8 Stream API. 

The hypothetical scenario would be the following: we want to pipeline some data using some transformations and 
do some side effects during some of those steps. E.g, we have a football league application and we want to check 
 before a match that a team is ready to play. Let's see the data in different moments of the pipeline:
 
1. Subscribed players

    a. (filter) Check if they're available for the match       

2. Available players

    a. (map) Create a team

3. Team ....             

Let's say that in 1.a we want to send an email to the guys that are available and in 2.a we want to store that team
in some DB. Those behaviours could and should live in different components, but conceptually that flow data is the same
stream. We want to use the data to execute the side effects but not consume it, as we want to keep using the stream.
 
Peek is a method in Java 8 Stream API that allows you to use but not consume the data of an stream. Then we could think
that is a good idea to use peek to call SendEmail component and TeamRepository. The problem with peek is that is only
executed whenever we do actually consume the stream. The Javadoc provides some clues about that:
*This method exists mainly to support debugging* and 
*additionally performing the provided action on each element as elements are consumed from the resulting stream.*

That *as elements are consumed* is the key but it's not obvious the first time you read it. In the provided class 
we can see how peek is executed only when a final operation (forEach in this case) is called:

```java
public static void main(String[] args) {
        IntStream stream = createAStreamAndPerformSomeSideEffectWithPeek();
        System.out.println("2. I should be the second group of prints");
        consumeTheStream(stream);
    }

private static IntStream createAStreamAndPerformSomeSideEffectWithPeek() {
    return IntStream.of(1, 2, 3)
            .peek(number -> System.out.println(String.format("1. My number is %d", number)))
            .map(number -> number + 1);
}

private static void consumeTheStream(IntStream stream) {
    stream.filter(number -> number % 2 == 0)
            .forEach(number -> System.out.println(String.format("3. My number is %d", number)));
}
```

We should expect an output like this:

- 1. My number is 1
- 1. My number is 2
- 1. My number is 3
- 2. I should be the second group of prints
- 3. My number is 2
- 3. My number is 4

When in fact we'll have:

- 2. I should be the second group of prints
- 1. My number is 1
- 3. My number is 2
- 1. My number is 2
- 1. My number is 3
- 3. My number is 4

