export type Artist = {
id : number;
name : string;
reports : {
id : number;
date : string;
place : string;
title : string;
text : string;
};
talentAgency : {
id : number;
country : string;
agencyName : string;
};
}