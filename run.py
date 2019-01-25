# Contributions by Timothy Barao, Francisco Escobar, and Brandon Polonia
import json
import datetime
import operator
from flask import Flask, request, jsonify, send_from_directory
from flask_sqlalchemy import SQLAlchemy
from models import Recipe, Ingredients, Users, UserList, RecipeList, UserRecList
from database import Base, db_session
from random import randint
from math import floor

app = Flask(__name__)

# Replace Database URI
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://localhost/PizzaPi'

db = SQLAlchemy(app)
folder = 'images'

@app.route('/getrecipeimage/<recname>/', methods=['GET'])
def GetRecipeImage(recname):
   if request.method == 'GET':
      return send_from_directory(folder, str(recname)+'.jpg')

@app.route('/recipe/', methods=['GET'])
def recipe():
   if request.method == 'GET':
      results = Recipe.query.all()
      json_results = []
      for i in results:
         json_results.append(i.name)

      json_results.sort()

      return "%s" % "\n".join(map(str, json_results))

@app.route('/userlist/<username>/', methods=['GET'])
def userlist(username):
   if request.method == 'GET':
      r = db_session.query(Users).filter(Users.username == username)
      for i in r:
         id = i.id
      results = db_session.query(Ingredients).filter(UserList.user_id == id).filter(UserList.ingredient_id == Ingredients.ingredient_id)
      json_results = []
      for i in results:
         json_results.append(i.name)

      json_results.sort()

      return "%s" % "\n".join(map(str, json_results))

@app.route('/userreclist/<username>/', methods=['GET'])
def userreclist(username):
   if request.method == 'GET':
      r = db_session.query(Users).filter(Users.username == username)
      for i in r:
         id = i.id
      results = db_session.query(Recipe).filter(UserRecList.user_id == id).filter(UserRecList.recipe_id == Recipe.recipe_id)
      json_results = []
      for i in results:
         json_results.append(i.name)

      json_results.sort()

      return "%s" % "\n".join(map(str, json_results))

@app.route('/ingredients/', methods=['GET'])
def ingredients():
   if request.method == 'GET':
      results = Ingredients.query.all()
      json_results = []
      for result in results:
         json_results.append(result.name)

      json_results.sort()

      return "%s" % "\n".join(map(str, json_results))

@app.route('/users/<search>', methods=['GET'])
def valid_user(search):
  if request.method == 'GET':
    results = Users.query.filter(Users.username == search)
    found = 0;
    for r in results:
       if r.username == search:
          found = 1
    if found == 1:
       return "true"
    else:
       return "false"

@app.route('/login/', methods=['POST'])
def login_user():
  if request.method == 'POST':
    username = request.form['username']
    password = request.form['password']
    results = Users.query.filter(Users.username == username).filter(Users.password == password)
    found = 0;
    for r in results:
       if r.username == username and r.password == password:
          found = 1
    if found == 1:
       return "true"
    else:
       return "false"

@app.route('/web/', methods=['POST'])
def web():
   from scraper import Recipes
   if request.method == 'POST':
      for i in Recipes:
         rec = db_session.query(Recipe).filter_by(name = i.name).first()
         if not rec:
            rec = Recipe(name=i.name, instructs = i.instructions, measure=i.measurements, desc=i.description)
            db_session.add(rec)
            db_session.commit()
            rec_id = db_session.query(Recipe).filter_by(name = i.name).first()

            for j in i.ingredients:
               ing = db_session.query(Ingredients).filter_by(name=j).first()
               if not ing:
                  ing = Ingredients(name=j)
                  db_session.add(ing)
               db_session.commit()
               ingred_id = db_session.query(Ingredients).filter_by(name = j).first()
               list = RecipeList(ingred_id.ingredient_id, rec_id.recipe_id)
            db_session.add(list)
            db_session.commit()
      return jsonify(items={'name': "Reaper"})

@app.route('/deleteItem/', methods=['POST'])
def deleteFromList():
   content = request.get_json(force=True)

   username = content['username']
   ingredient = content['ingredient']
   user = db_session.query(Users).filter_by(username = username).first()
   ingred = db_session.query(Ingredients).filter_by(name = ingredient).first()
   x = db_session.query(UserList).filter(UserList.user_id == user.id).filter(UserList.ingredient_id == ingred.ingredient_id).delete()
   db_session.commit()
   return 'true'

@app.route('/addItem/', methods=['POST'])
def addToList():
   #Make sure cant add/delete twice
   content = request.get_json(force=True)
   username = content['username']
   ingredient = content['ingredient']
   user = db_session.query(Users).filter(Users.username == username).first()
   ingred = db_session.query(Ingredients).filter_by(name = ingredient).first()
   x = UserList(ingredient_id = ingred.ingredient_id, user_id = user.id)
   db_session.add(x)
   db_session.commit()
   return 'true'

@app.route('/deleteRecipe/', methods=['POST'])
def deleteRecFromList():
   content = request.get_json(force=True)
   username = content['username']
   recipe   = content['recipe']

   user = db_session.query(Users).filter_by(username = username).first()
   rec = db_session.query(Recipe).filter_by(name = recipe).first()

   x = db_session.query(UserRecList).filter(UserRecList.user_id == user.id).filter(UserRecList.recipe_id == rec.recipe_id).delete()

   db_session.commit()
   return 'true'

@app.route('/addRecipe/', methods=['POST'])
def addRecToList():
   content = request.get_json(force=True)
   username = content['username']
   recipe   = content['recipe']
   user = db_session.query(Users).filter(Users.username == username).first()
   rec = db_session.query(Recipe).filter_by(name = recipe).first()

   x = UserRecList(recipe_id = rec.recipe_id, user_id = user.id)
   db_session.add(x)
   db_session.commit()
   return 'true'

@app.route('/trueFilter/<userName>/', methods=['GET'])
def trueFilter(userName):
   if request.method == 'GET':
      us = userName
      user = db_session.query(Users).filter_by(username = us).first()
      userlist = db_session.query(UserList).filter(UserList.user_id == user.id)

      ids = []
      for i in userlist:
         ids.append(i.ingredient_id)

      x = RecipeList.query.all()
      recipes = []

      id = x[0].recipe_id
      bad_id = [-1]

      percentMiss = int(floor(len(ids) * 0.30))
      missing = 0
      misses = []
      dict = {}
      counter = 0

      for i in x:
         counter += 1
         if i.recipe_id not in dict and i.recipe_id not in bad_id:
           dict[i.recipe_id] = 0
         if i.recipe_id == id and i.ingredient_id not in ids and i.recipe_id not in bad_id:
           dict[i.recipe_id] = int(dict[i.recipe_id]) +  1

         if i.recipe_id != id:
           if int(dict[id]) > int(floor(counter * 0.30)):
             if id in dict:
               del dict[id]

             bad_id.append(id)

           id = i.recipe_id
           counter = 0

      if len(dict) == 1:
        key = -1
        for i in dict:
           if int(dict[i]) > int(floor(counter * 0.30)):
              key = i
        if key != -1:
           del dict[key]

      dict = sorted(dict.items(), key=operator.itemgetter(1))

      for key in dict:
         recipes.append(key[0])

      results = db_session.query(Recipe).filter(Recipe.recipe_id.in_(recipes))

      json_results = []

      for i in results:
         json_results.append(i.name)

      return "%s" % "\n".join(map(str, json_results))

@app.route('/create/', methods=['POST'])
def create_user():
    if request.method == 'POST':
        un = request.form['username']
        pw = request.form['password']
        new_user = Users(username=un, password=pw)
        db_session.add(new_user)
        db_session.commit()
    return 'true'

@app.route('/recipepage/<rname>/<uname>/', methods=['GET'])
def recipe_page(rname, uname):
  r = db_session.query(Recipe).filter_by(name = rname).first()
  uid = db_session.query(Users).filter_by(username = uname).first()
  u = db_session.query(UserList).filter_by(user_id = uid.id)

  ids = []

  for i in u:
    ids.append(i.ingredient_id)

  ingreds = db_session.query(Ingredients).filter(Ingredients.ingredient_id.in_(ids))

  ingredients = []

  for i in ingreds:
     ingredients.append(i.name)

  measures     = []
  missMeasures = []
  splitMeasurements = r.measurements.split('\n')

  for i in ingredients:
    for j in splitMeasurements:
       if i in j and j not in measures:
          measures.append(j)

  for i in splitMeasurements:
     if i not in measures:
        missMeasures.append(i)

  M = ""
  m = ""

  for i in measures:
     M += str(i +'\n')

  for i in missMeasures:
     m += str(i +'\n')


  d = {'Name': r.name,
       'Description': r.description,
       'Measurements': M,
       'MissingMeasurements': m,
       'Instructions': r.instructions}

  return jsonify(**d)

@app.route('/rotd/<username>/', methods=['GET'])
def recipeOfTheDay(username):
  if request.method == 'GET':
    now = datetime.datetime.now()
    today = now.day
    r = db_session.query(Recipe).filter(Recipe.recipe_id == 0).first()
    return recipe_page(r.name, username)

if __name__ == '__main__':
   app.run(host="0.0.0.0", port=5000, debug=True)
