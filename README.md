# pricebasket

Write a program and associated unit tests that can price a basket of goods, accounting for special offers.
<br/>
The goods that can be purchased, which are all priced in GBP, are:
<ul>
<li>Soup – 65p per tin</li>
<li>Bread – 80p per loaf</li>
<li>Milk – £1.30 per bottle</li>
<li>Apples – £1.00 per bag</li>
</ul>
Current special offers are:
<ul>
<li>Apples have 10% off their normal price this week</li>
<li>Buy 2 tins of soup and get a loaf of bread for half price</li>
</ul>
The program should accept a list of items in the basket and output the subtotal, the special offer discounts and the final
price.
<br/><br/>
Input should be via the command line in the form PriceBasket item1 item2 item3 …
<br/>
For example: PriceBasket Apples Milk Bread
<br/><br/>
Output should be to the console, for example:<br/>
<code>Subtotal: £3.10</code><br/>
<code>Apples 10% off: -10p</code><br/>
<code>Total: £3.00</code><br/>
If no special offers are applicable, the code should output: <br/>
<code>Subtotal: £1.30</code><br/>
<code>(no offers available)</code><br/>
<code>Total: £1.30</code><br/><br/>
The code and design should meet these requirements but be sufficiently flexible to allow for future extensibility. The
code should be well structured, suitably commented, have error handling and be tested.
