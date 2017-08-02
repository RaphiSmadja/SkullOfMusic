"use strict";

module.exports = function(sequelize, DataTypes) {
	const likeUnlike = sequelize.define("likeUnlike", {
		likeId: {
			type: DataTypes.BIGINT,
			primaryKey: true,
			autoIncrement: true
		},musicId: {
			type: DataTypes.BIGINT
		},
		likerUnlikerId: {
			type: DataTypes.BIGINT
		},
		like: {
			type: DataTypes.BIGINT
		}
	}, {
		paranoid: true,
		freezeTableName: true
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

	return likeUnlike;
};