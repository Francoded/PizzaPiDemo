#Contributions by Timothy Barao and Francisco Escobar
import sqlalchemy
from sqlalchemy.sql import select
from sqlalchemy import Table, Column, Integer, String, ForeignKey
from database import Base, metadata

class Recipe(Base):
   __tablename__ = 'recipe'
   recipe_id = Column(Integer, primary_key=True)
   name = Column(String)
   description = Column(String)
   instructions = Column(String)
   measurements = Column(String)

   def __init__(self, name, instructs, measure, desc):
      self.name = name
      self.instructions = instructs
      self.measurements = measure
      self.description = desc

class FoodBank(Base):
  __tablename__ = 'food_bank'
  name = Column(String, primary_key=True)

  def __init__(self, name):
     self.name = name

class Ingredients(Base):
   __tablename__ = 'ingredients'
   ingredient_id = Column(Integer, primary_key=True)
   name = Column(String)

   def __init__(self, name):
      self.name = name

class Users(Base):
	__tablename__ = 'users'
	id = Column(Integer, primary_key=True)
	username = Column(String)
	password = Column(String)

	def __init__(self, username, password):
		self.username = username
		self.password = password

class UserList(Base):
        __tablename__ = 'userlist'
        ingredient_id = Column(Integer, ForeignKey(Ingredients.ingredient_id), nullable=False, primary_key=True)
        user_id = Column(Integer, ForeignKey(Users.id), nullable = False, primary_key=True)

        def __init__(self, ingredient_id, user_id):
                self.ingredient_id = ingredient_id
                self.user_id = user_id

class UserRecList(Base):
        __tablename__ = 'favrecipelist'
        recipe_id = Column(Integer, ForeignKey(Recipe.recipe_id), nullable=False, primary_key=True)
        user_id = Column(Integer, ForeignKey(Users.id), nullable = False, primary_key=True)

        def __init__(self, recipe_id, user_id):
                self.recipe_id = recipe_id
                self.user_id = user_id

class RecipeList(Base):
        __tablename__ = 'recipelist'
        ingredient_id = Column(Integer, ForeignKey(Ingredients.ingredient_id), nullable=False, primary_key=True)
        recipe_id = Column(Integer, ForeignKey(Recipe.recipe_id), nullable=False, primary_key=True)

        def __init__(self, ingredient_id, recipe_id):
                self.ingredient_id = ingredient_id
                self.recipe_id     = recipe_id
