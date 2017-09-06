"use strict";

module.exports = function(sequelize, DataTypes) {
	const signalize = sequelize.define("signalize", {
		signalizeId: {
			type: DataTypes.BIGINT,
			primaryKey: true,
			autoIncrement: true
		},
		musicId: {
			type: DataTypes.BIGINT
		},
		flagmanId: {
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

	return signalize;
};