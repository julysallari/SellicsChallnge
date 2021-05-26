# Sellics Challenge
REST API to calculate the estimation of Amazon's search-volume of a given keyword, in a range between 0 and 100; being a score of 100 for the most searched keywords, and 0 almost never searched for.

The application was built using Gradle, Spring Boot and Docker.

## Getting Started

- To get started locally with Spring Boot:

./gradlew bootRun

- To build a Docker container:

docker build -t jsallari/sellics .

## API

- How it works:
It is based on different level of what I called Tiers; a tier 1 keyword is found on Amazon's completion API response with only the first letter of the keyword as input. Let's say that we have Nodes in the tiers; so, a tier one node contains hot keywords.
Then the algorithm keeps calling Amazon's API by adding the following letter of the keyword. Here, there are two choices:
 1. The following call response does not bring back any of the keywords of the previous calls, which - I assumed - it means it looked for keywords in a level down tier node. As for that, it adds an "addedTier" to calculate teh score.
 2. The following call response brings back some words of the previous tier, so, it means it looked for keywords in a level down node or, in a node of the same tier but didn't make the cut on the first 10 hot keywords previously.
Then, for each tier the algorithm has visited, it downs 10 points and 10 extra points for addedTiers - I assumed that an abrupt jump on a tier represents an even lower score.
Lastly, starting on the lowest calculated score so far, it increases the score by one depending on the position of the list the keyword was found in the response which contained it.
At the beginning of the score computing algorithm, it searches for the whole keyword; if it wasn't found by Amazon's API, the score is 0

- Precision:
There are corner cases like 3 letters words, for example, that I found them moe difficult to asses whether they have an accurate score.
Overall, I'd give it a 70% accurate rate.


## How to get a score

``curl http://localhost:8080/search?q=iphone+charger``


### Other
I took the liberty of setting a timeout for the Amazon API call of 1 second and to not call the API more that 10 times. In case there is a delayed response, the system would fail to give a response.
