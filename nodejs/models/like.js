"use strict";

module.exports = function(sequelize, DataTypes) {
	const like = sequelize.define("like", {
		likeId: {
			type: DataTypes.BIGINT,
			primaryKey: true,
			autoIncrement: true
		},musicId: {
			type: DataTypes.BIGINT
		},
		likerId: {
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

	return like;
};