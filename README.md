Open Recommendation Platform - Java SDK
=======================================
The Open Recommendation Platform (ORP) is a distributed platform of entities capable of delivering recommendations for various purposes. It consists of recommendation providers and recommendation consumers that interact and communicate over a standardized protocol. This document describes the protocol and outlines the necessary steps a partner needs to take in order to integrate a technology as recommendation provider. The overall aim of the ORP is to obtain a better recommendation quality. In the context of advertising, better recommendations are defined by a higher CPM (cost per impression). In the context of on-site recommendations, better recommendations are defined by a higher CTR (click-through-rate).

This is a short description of the *Java* implementation for the *News Recommender Systems* challenge hosted by *plista*. We outline the essential components. Additionally, we emphasise on how to incorporate your own recommender.

Technical Restrictions
----------------------
Please ensure your system is able to reply within 100ms, as response time is critical for our application. Please further ensure that your system can handle the amount of incoming data. Expect up to several thousand requests per second. When we detect a performance problem, we may automatically decrease the amount of requests forwarded to your system.

OVERVIEW CLASSES
----------------

+ ContestHandler.java
+ RecommendedItem.java
+ RecommenderItemTable.java
+ DirtyRingBuffer.java

COMMUNICATION WITH THE ORP SERVER
---------------------------------

The communication is handled by *Client.java* and *ContestHandler.java*. The *Client.java* receives the messages and forwards their contents to the *ContestHandler.java* who delegates the messages to the suited methods. There are four basic types of messages:

+ Item updates
+ Recommendation Request
+ Notifications (ipmressions, clicks)
+ Errors

Those message types are encoded as HTTP-POST-Parameter. The *Client.java* extracts the type information. Subsequently, the *ContestHandler.java* initiates the pre-defined way to handle the request.

RECOMMENDER BASELINE
--------------------

The implementation includes a baseline recommender. Considering recency as the most important factor represents the basic idea of the baseline. Each publisher (identified by *context.simple.27*) is assigned a list of fixed size. As new items arrive - either by updates/creates or through notifications (clicks/impressions) - the item Ids (identified by *context.simple.25*) are added to the list. Upon incoming recommendation requests, the *DirtyRingBuffer.java* returns the *N* most recent items. *N* refers to the number of items requested. As soon as the list capacity is exceeded, the least recent items are dropped.

ITEM REPRESENTATION
-------------------

We represent items as instances of *RecommenderItem.java*. Those instances provide access to data encoded in the JSON. When creating the *RecommenderItem.java*, the JSON is parsed and essential information such as item Id, publisher Id, and timestamp. The *RecommenderItem.java* instances are subsequently gathered in an array representation - *RecommenderItemTable.java*.

REQUIRED ADAPTIONS
------------------

If you want to use this template to write your own recommender, you can use the item representations. Both *Client.java* and *ContestHandler.java* offer the opportunity to include a recommender. Those recommenders must return a *List<Long>* in order to create an adequate response. Note that you have to adjust the *properties* file to support the port used to communicate with the server. Further information can be found in <http://orp.plista.com>.

Licence
----------

MIT License, (c) 2013, TU Berlin
