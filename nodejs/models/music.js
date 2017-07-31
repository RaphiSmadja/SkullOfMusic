"use strict";

module.exports = function(sequelize, DataTypes) {
	const Music = sequelize.define("Music", {
		idMusic: {
			type: DataTypes.BIGINT,
			primaryKey: true,
			autoIncrement: true
		},
		ownerIdMusic: {
			type: DataTypes.BIGINT
		},
		Title: {
			type: DataTypes.STRING
		},
		artist: {
			type: DataTypes.STRING
		},
		pathMusic: {
			type: DataTypes.STRING
		},
		pathPicture: {
			type: DataTypes.STRING
		},
		gender: {
			type: DataTypes.STRING
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
		}
	});
	return Music;
};
