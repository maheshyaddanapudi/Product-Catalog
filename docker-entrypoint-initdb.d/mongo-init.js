print('Start #################################################################');

db = db.getSiblingDB('catalog');
db.createUser(
  {
    user: 'catalog',
    pwd: 'Catalog!234',
    roles: [{ role: 'readWrite', db: 'catalog' }],
  },
);
db.createCollection('users');

print('END #################################################################');