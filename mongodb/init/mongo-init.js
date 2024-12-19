db = db.getSiblingDB('admin');
db.createUser({
  user: 'admin',
  pwd: 'secret',
  roles: ['root']
});

db = db.getSiblingDB('ayziwai_prod');
db.createUser({
  user: 'admin',
  pwd: 'secret',
  roles: [
    { role: 'readWrite', db: 'ayziwai_prod' },
    { role: 'dbAdmin', db: 'ayziwai_prod' }
  ]
}); 