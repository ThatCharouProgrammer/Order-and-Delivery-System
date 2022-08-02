/* ORDER
Most ordered product for each order: 
For each order has many order details. From the list of items ordered for each order with the highest quantity, display the
order ID, name and unit price of the product ordered, quantity of that item requested and the total price for that order detail.
*/

SELECT orderId, productName, unitPrice, quantityRequested, totalPrice
	FROM product, order_details AS T
		WHERE T.productId = product.productId AND quantityRequested = 
			(SELECT MAX(quantityRequested) FROM order_details WHERE orderId = T.orderId)	
		GROUP BY orderId;
					
/*
Drivers that made the most deliveries:
Display the employees ID, fullname and number of deliveries made
*/

SELECT dEmployeeId, firstName, surname, COUNT(dEmployeeId) AS Number_of_Deliveries
	FROM delivery, employee AS T
		WHERE dEmployeeId = T.employeeId AND dEmployeeId = 
			(SELECT DISTINCT employeeId FROM driver WHERE dEmployeeId = employeeId)
			GROUP BY dEmployeeId 
			ORDER BY Number_of_Deliveries DESC;
			
/* ORDER
Customer order and delivery report
*/
			
SELECT customer.customerId, firstname, surname, orderId, totalOrderPrice, paid, delivery.deliveryId, deliveryStatus FROM order_tbl
INNER JOIN customer ON order_tbl.customerId = customer.customerId
INNER JOIN delivery ON order_tbl.deliveryId = delivery.deliveryId;


/*
Number of completed orders
*/

SELECT COUNT(customer.customerId) AS COMPLETED_ORDERS FROM customer
INNER JOIN order_tbl ON customer.customerId = order_tbl.customerId AND paid = "YES"
INNER JOIN delivery ON order_tbl.deliveryId = delivery.deliveryId AND deliveryStatus = "DELIVERED";

/*
Most popular products
*/

SELECT productName, unitPrice, categoryId, AVG(rating) AS Rating FROM product, product_review 
WHERE product.productId = product_review.productId 
GROUP BY productName ORDER BY Rating DESC LIMIT 10;



