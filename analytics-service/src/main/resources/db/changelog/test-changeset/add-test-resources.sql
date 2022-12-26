delete from analytical_data;

insert into analytical_data (product_id, order_id, username, quantity, price_per_product, bought_at)
values (1, 1, 'bob', 2, 40.07, '2022-12-15 01:00:00'),
       (2, 2, 'bob', 1, 100.34, '2022-12-13 01:00:00'),
       (1, 1, 'bob', 3, 40.07, '2022-12-11 01:00:00'),
       (3, 1, 'bob', 1, 150.12, '2022-12-14 01:00:00'),
       (1, 3, 'john', 2, 40.07, '2022-12-10 01:00:00'),
       (3, 2, 'bob', 1, 150.12, '2022-12-09 01:00:00')