from sqlalchemy import create_engine, MetaData
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base

# Replace Database URI
engine = create_engine('mysql://pizzapi:pizzapi@localhost/PizzaPi', convert_unicode=True)
db_session = scoped_session(sessionmaker(autocommit=False,
                                         autoflush=False,
                                         bind=engine))

metadata = MetaData()

Base =  declarative_base()
Base.query = db_session.query_property()
