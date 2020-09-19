<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class jenisMenuDetail extends Model
{
    protected $primaryKey = 'jenis_menu_detail_id';
    
    public function jenisMenu(){
        return $this->belongsTo('App\jenisMenu','jenis_menu_id','jenis_menu_id');
    }
    
    public function makanan(){
        return $this->hasOne('App\makanan','makanan_id','makanan_id');
    }
}
