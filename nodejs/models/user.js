"use strict";

module.exports = function(sequelize, DataTypes) {
	const User = sequelize.define("User", {
		id: {
			type: DataTypes.BIGINT,
			primaryKey: true,
			autoIncrement: true
		},
		pseudo: {
			type: DataTypes.STRING
		},
		emailAddress: {
			type: DataTypes.STRING
		},
		lastName: {
			type: DataTypes.STRING
		},
		firstName: {
			type: DataTypes.STRING
		},
		hashedPassword: {
			type: DataTypes.STRING
		},
		picture: {
			type: DataTypes.STRING
		},
		isAdmin: {
			type: DataTypes.BIGINT
		}
	}, {
		paranoid: true,
		freezeTableName: true 
		// désactive la modification des noms de tableaux; Par défaut, la mise à jour automatiquement
		// transforme tous les noms de modèles passés (premier paramètre de définition) en pluriel.
		// si vous ne voulez pas cela, définissez le suivant
	}, {
		classMethods: {
			associate: function(models) {
			}
		},
		 instanceMethods: {
                responsify: function() {
                    return {
                        name: this.id + ' : ' + this.firstName + ' ' + this.lastName,
                        email: this.emailAddress
                    };
                }
            }
	});
	return User;
};
