# cs2030servers
Semester assessment for my NUS cs2030 module. Simulates a group of server serving a group of customers using random generators.

Input to the program comprises (in order of presentation):

  an int value denoting the base seed for the RandomGenerator object;
  an int value representing the number of servers
  an int value representing the number of self-checkout counters, Nself
  an int value for the maximum queue length, Qmax
  an int representing the number of customers (or the number of arrival events) to simulate
  a positive double parameter for the arrival rate, λ
  a positive double parameter for the service rate, μ
  a positive double parameter for the resting rate, ρ
  a double parameter for the probability of resting, Pr
  a double parameter for the probability of a greedy customer occurring, Pg
  
Example input: 1 2 1 2 20 1.0 1.0 0.1 0.5 0.9

Customer Queues
Each server now has a queue of customer to allow multiple customers to queue up. A customer that chooses to join a queue joins at the tail. When a server is done serving a customer, it serves the next waiting customer at the head of the queue. Hence, the queue should be a first-in-first-out (FIFO) structure.

Taking a Rest
The servers are allowed to take occasional breaks. When a server finishes serving a customer, there is a probability Pr that the server takes a rest for a random amount of time Tr. During the break, the server does not serve the next waiting customer. Upon returning from the break, the server serves the next customer in queue immediately.

To implement this behavior, introduce two new events, SERVER_REST and SERVER_BACK, to simulating taking a break, and returning. These events should be generated and scheduled in the simulator when the server decides to rest.

Self-Checkout
To reduce waiting time, self-checkout counters have been set-up. These self-checkout counters never need to rest. Customers queue up for the self-checkout counter in the same way as human servers. There is one queue per self-checkout counter.
Customers' Choice of Queue
As before, when a customer arrives, he or she first scans through the servers (in order, from 1 to k) to see if there is an idle server (i.e. not serving a customer and not resting). If there is one, the customer will go to the server to be served. Otherwise, a typical customer just chooses the first queue (while scanning from servers 1 to k) that is still not full to join. However, other than the typical customer, a greedy customer is introduced that always chooses the queue with the fewest customers to join. In the case of a tie, the customer breaks the tie by choosing the first one while scanning from servers 1 to k.

If a customer cannot find any queue to join, it will leave the shop.
