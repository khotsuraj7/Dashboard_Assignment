
-- sellers table
CREATE TABLE IF NOT EXISTS sellers (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  region VARCHAR(100)
);

-- sales table
CREATE TABLE IF NOT EXISTS sales (
  id SERIAL PRIMARY KEY,
  seller_id INTEGER REFERENCES sellers(id),
  date DATE,
  quantity INTEGER,
  price NUMERIC(12,2),
  returned BOOLEAN DEFAULT false
);

-- sample sellers
INSERT INTO sellers (name, region) VALUES ('TechZone', 'West'), ('HomeGoods', 'East') ON CONFLICT DO NOTHING;

-- sample sales (last 14 days)
DO $$
BEGIN
  IF (SELECT COUNT(*) FROM sales) = 0 THEN
    INSERT INTO sales (seller_id, date, quantity, price, returned)
    SELECT 1, current_date - (i % 14), (3 + (i % 5)), (50 + (i % 10))*1.0, (i % 10 = 0)
    FROM generate_series(1, 40) s(i);
    INSERT INTO sales (seller_id, date, quantity, price, returned)
    SELECT 2, current_date - (i % 14), (1 + (i % 4)), (20 + (i % 15))*1.0, (i % 15 = 0)
    FROM generate_series(1, 30) s(i);
  END IF;
END$$;
